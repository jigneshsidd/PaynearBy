package com.example.paynearby

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.paynearby.DATABASE.User
import com.example.paynearby.DATABASE.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity(), TopFragment.topFragmentInterface, BottomFragment.bottomFragmentInterface {

    var userViewModel: UserViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val f: TopFragment = fm.findFragmentById(R.id.topLL) as TopFragment
        val bottomeFm: FragmentManager = supportFragmentManager
        val f1: BottomFragment = bottomeFm.findFragmentById(R.id.bottomLL) as BottomFragment
        shuffle!!.setOnClickListener {
            runBlocking {
                val async = async {
                    if (f.topImageList != null && f.topImageList!!.size > 0) {
                        val topRandom = (0..f.topImageList!!.size).random()
                        f.binding!!.viewPagerImageSlider.currentItem = topRandom
                    }

                    if (f1.bottomImageList != null && f1.bottomImageList!!.size > 0) {
                        val bottomRandom = (0..f1.bottomImageList!!.size).random()
                        f1.binding!!.viewPagerImageSlider.currentItem = bottomRandom
                    }
                }
                async.await()
            }
        }

//        var fragment = supportFragmentManager.findFragmentById(R.id.top) as TopFragment
//        fragment.callAboutUsActivity()
        userViewModel = UserViewModel(application)
        save.setOnClickListener {

            var topCurrentItem = f.showToast()

            var bottomCurrentItem = f1.showToast()


            if(topCurrentItem != "" && bottomCurrentItem != "") {
                Log.d("topBottom", "" + topCurrentItem + " " + bottomCurrentItem)
                insertToDb(topCurrentItem,bottomCurrentItem) //Storing user prefrence value
            } else {
                Toast.makeText(applicationContext,"No Image is present",Toast.LENGTH_LONG).show()
            }
        }

        userViewModel!!.readAllData.observe(this, Observer {
            if(it != null && it.size > 0) {
                Log.d("UserData", "" + it.get(0)+" size"+it.size)
                f.topImageList = it.map { it.topImage }.toMutableList()
                f1.bottomImageList = it.map { it.bottomImage }.toMutableList()
                f.setViewPager()
                f1.setViewPager()
            }
        })
    }

    private fun insertToDb(topCurrentItem: String, bottomCurrentItem: String) {
        runBlocking {
            var s = async {
                var user = User(topCurrentItem,bottomCurrentItem)
                userViewModel!!.addUser(user)
            }
            s.await()

            var s1 = async {
                var data = userViewModel!!.readAllUserData()
            }
            s1.await()
        }
    }

    override fun onSaveTop(msg: String?) {
//        Toast.makeText(applicationContext,"Top Fragment returned",Toast.LENGTH_LONG).show()
    }

    override fun onSaveBottom(msg: String?) {
//        Toast.makeText(applicationContext,"Bottome Fragment returned",Toast.LENGTH_LONG).show()
    }
}