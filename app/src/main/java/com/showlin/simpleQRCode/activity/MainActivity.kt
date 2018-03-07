package com.showlin.simpleQRCode.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import com.showlin.simpleQRCode.R
import com.showlin.simpleQRCode.Utility
import com.showlin.simpleQRCode.Utility.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager.TaskDescription
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Bitmap




class MainActivity : BaseActivity() {
    var isSwitch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val bm = BitmapFactory.decodeResource(resources, R.drawable.logo)
            val taskDesc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TaskDescription(getString(R.string.app_name), bm, ContextCompat.getColor(this, R.color.color1))
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
            setTaskDescription(taskDesc)
        }


        btn_capture.setOnClickListener{
            val animation = RotateAnimation(0f, -360f, Animation.ABSOLUTE, btn_capture.pivotX, Animation.ABSOLUTE, btn_capture.pivotY + 100)
            animation.duration = 600
            animation.interpolator = object : AccelerateInterpolator(){}
            img_bear_white.startAnimation(animation)
            animation.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    runOnUiThread {Utility.scan(this@MainActivity)}
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }

        img_bear_white.setOnClickListener {
            val animation = RotateAnimation(0f, -360f, Animation.ABSOLUTE, btn_capture.pivotX, Animation.ABSOLUTE, btn_capture.pivotY + 100)
            animation.duration = 600
            animation.interpolator = object : AccelerateInterpolator(){}
            img_bear_white.startAnimation(animation)
        }

        btn_generate.setOnClickListener {
            val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            animation.duration = 300
            img_bear_brown.startAnimation(animation)
            animation.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    runOnUiThread {
                        val intent = Intent(this@MainActivity, GenerateActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }

        img_bear_brown.setOnClickListener {
            val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            animation.duration = 300
            img_bear_brown.startAnimation(animation)
        }

        text_title.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        text_title.setOnClickListener{
            isSwitch = !isSwitch
            val oneX = img_bird_1.x
            val twoX = img_bird_2.x
            val offset = Math.abs(oneX - twoX)

            val animation = TranslateAnimation(0f, offset, 0f, 0f)
            animation.duration = 300
            animation.isFillEnabled = true
            animation.fillAfter = true
            animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (isSwitch){
                        img_bird_1.setImageResource(R.drawable.img_bird_2)
                    }else{
                        img_bird_1.setImageResource(R.drawable.img_bird_1)
                    }
                    img_bird_1.clearAnimation()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
            img_bird_1.startAnimation(animation)


            val animation2 = TranslateAnimation(0f, -offset, 0f, 0f)
            animation2.duration = 300
            animation2.isFillEnabled = true
            animation2.fillAfter = true
            animation2.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (isSwitch){
                        img_bird_2.setImageResource(R.drawable.img_bird_1)
                    }else{
                        img_bird_2.setImageResource(R.drawable.img_bird_2)
                    }
                    img_bird_2.clearAnimation()
                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            img_bird_2.startAnimation(animation2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                val bundleResult = data!!.extras
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtras(bundleResult)
                startActivity(intent)
            }
        }
    }
}
