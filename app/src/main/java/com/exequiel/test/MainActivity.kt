package com.exequiel.test

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        btnShare.setOnClickListener {
            val link = "I just joined <>! Sign up too so we can communicate anytime, anywhere! https://play.google.com/store/apps/details?id=com.facebook.katana"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, link)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "Share <<>> via: "))

            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(sendIntent, "Share <<>> via: "))
//                startActivity(sendIntent)
            }

        }

        btnSMSshare.setOnClickListener {
            val link = "I just joined <>! Sign up too so we can communicate anytime, anywhere! https://play.google.com/store/apps/details?id=com.facebook.katana"

            //  share to sms
            /*
            val sendIntent = Intent().apply {
                putExtra(Intent.EXTRA_TEXT, link)
                type = "vnd.android-dir/mms-sms"
                    }

             // startActivity(sendIntent)
             startActivity(Intent.createChooser(sendIntent, "Share <><> via: "))
             */

            // /*
            // direct share via default sms app
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                val defaultSMS = Telephony.Sms.getDefaultSmsPackage(this)

                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, link)

                if (defaultSMS != null) {
                    sendIntent.setPackage(defaultSMS)
                }
                startActivity(sendIntent)

            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }
//             */
        }

        navRingtone.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        btnPlay.setOnClickListener() {
            items()
        }


        btnSetRingtone.setOnClickListener {
            val rm: Uri? = RingtoneManager.getActualDefaultRingtoneUri(
                this,
                RingtoneManager.TYPE_RINGTONE
            )

            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone".toUpperCase())
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, rm)
            startActivityForResult(intent, 999)

            val r = RingtoneManager.getRingtone(this, rm)
            if (r != null) {
                r.stop()
            } else {
                r?.play()
            }

            /*
            try {
                val path: Uri = Uri.parse("android.resource://$packageName" + rm.toString())
                RingtoneManager.setActualDefaultRingtoneUri(
                    this,
                    RingtoneManager.TYPE_RINGTONE,
                    path
                )
                val r = RingtoneManager.getRingtone(this, path)
                r.play()
            } catch (e: Exception) {
                e.localizedMessage?.toString()
            } */
        }
    }

    private fun play(rm: String) {

    }

    private fun items() {
//        val items = listOf("apple", "banana", "kiwifruit")
//        var index = 0
//        while (index < items.size) {
//            println("item at $index is ${items[index]}")
//            index++
//        }

        /*
        * java code
        * for (int i = 0; i < 5; i++) {    }
        * // outputs 0, 1, 2, 3, 4
        * */

        // this method is called `Progression` in Kotlin
        for (i in 1..10) {
            Log.d("test", i.toString())
        }

        // println((1..10).filter { it % 2 == 0 }) // outputs in array list

        /**
         * above codes have the same output
         **/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == RESULT_OK) {
            val rm: Uri? = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)

            sharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("TEST", rm.toString())
            editor.apply()

            tvRingtone.text = rm.toString()
        }
    }

    private fun loadData() {
        sharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
        tvRingtone.text = sharedPreferences.getString("TEST", "")
    }

}