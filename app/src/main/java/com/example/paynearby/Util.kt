package com.example.paynearby

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import java.io.ByteArrayOutputStream

public class Util {
    companion object{
        public fun openGalleryForImage(activity: FragmentActivity?) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activity!!.startActivityForResult(intent, 68)
        }

        public fun getRealPathFromURI(uri: Uri?, context: Context?): String? {
            var path = ""
            if (context!!.getContentResolver() != null) {
                val cursor: Cursor = context!!.getContentResolver().query(uri!!, null, null, null, null)!!
                if (cursor != null) {
                    cursor.moveToFirst()
                    val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    path = cursor.getString(idx)
                    cursor.close()
                }
            }
            return path
        }

        public fun getImageUri(inImage: Bitmap, context: Context?): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context!!.getContentResolver(), inImage, "Title", null)
            return Uri.parse(path)
        }
    }
}