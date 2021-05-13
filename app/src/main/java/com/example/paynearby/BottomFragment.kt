package com.example.paynearby

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.paynearby.databinding.FragmentBottomBinding
import kotlinx.android.synthetic.main.fragment_top.viewPagerImageSlider
import java.io.ByteArrayOutputStream
import java.io.File

class BottomFragment : Fragment() {
    var bottomImageList: MutableList<String> ? = mutableListOf()
    var binding: FragmentBottomBinding? = null

    interface bottomFragmentInterface {
        fun onSaveBottom(msg: String?)
    }

    var mListener: bottomFragmentInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is bottomFragmentInterface) {
            mListener = context as bottomFragmentInterface
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.addImage.setOnClickListener {
            requestPermission()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomBinding.inflate(layoutInflater)
        return  binding!!.root
    }

    fun requestPermission(){
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions( arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 69)
//            requestPermissions((context as Activity?)!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 69)
        } else {
            selectImage()
        }
    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 200)
            } else if (options[item] == "Choose from Gallery") {
//                Util.openGalleryForImage(activity)
                openGalleryForImage()
//                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    public fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 68)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200 && data != null){
            var bitmap: Bitmap = data.extras!!.get("data") as Bitmap
//            bitmap.
//            Log.d("ImagePath", "" + getImageUri(bitmap))
            val tempUri = Util.getImageUri(bitmap,context)
            Log.d("tempUri", "" + tempUri)
            val finalFile = File(Util.getRealPathFromURI(tempUri,context))
            Log.d("finalPath", "" + finalFile.absolutePath)
//            Log.d("finalPath", "" + finalFile.path)
            bottomImageList!!.add(finalFile.absolutePath)
            setViewPager()
//            binding!!.showTopImage.setImageURI(Uri.parse(finalFile.absolutePath))
//            binding!!.showTopImage.setImageBitmap(data.extras!!.get("data") as Bitmap)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 68){
            var selectedImageUri: Uri = data!!.data!!
            getPath(selectedImageUri)
//            Log.d("galleryPath", "" + data!!.data!!.path)
//            binding!!.showTopImage.setImageURI(Uri.parse(str)) // handle chosen image
        }
    }

    private fun getPath(selectedImageUri: Uri) {
        // Get the path from the Uri
        // Get the path from the Uri
        var path: String = getPathFromURI(selectedImageUri)!!
        if (path != null) {
            val f = File(path)
            var selectedImageUri = Uri.fromFile(f)
            Log.d("galleryPath",""+selectedImageUri)
            bottomImageList!!.add(selectedImageUri.toString())
            setViewPager()
//            binding!!.showTopImage.setImageURI(selectedImageUri) // handle chosen image
        }
        // Set the image in ImageView
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = context!!.getContentResolver().query(contentUri!!, proj, null, null, null)!!
        if (cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("requestCode",""+requestCode)
        when (requestCode) {

            69 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                selectImage()
                // main logic
            } else {
                Toast.makeText(context, "Please grant the required permission", Toast.LENGTH_SHORT).show()
                requestPermission()
            }

        }
    }

    fun setViewPager(){
        var adapter = MyCustomAdapter(bottomImageList, context!!)
        viewPagerImageSlider.adapter = adapter
        viewPagerImageSlider.refreshDrawableState()
    }

    fun showToast(): String {
        if(bottomImageList != null && bottomImageList!!.size > 0) {
            return bottomImageList!!.get(viewPagerImageSlider!!.currentItem)
        } else {
            return ""
        }
//        mListener!!.onSaveBottom("Hello")
//        Toast.makeText(context, "Hello bhai " + topImageList!!.size, Toast.LENGTH_LONG).show()
    }
}