package com.showlin.simpleQRCode.activity

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.showlin.simpleQRCode.R
import com.showlin.simpleQRCode.Utility.KEY_RESULT
import kotlinx.android.synthetic.main.activity_scan.*


class ScanActivity : Activity(){
    private var isTorch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setContentView(R.layout.activity_scan)
        barcode_view.decodeContinuous(callback)

        img_flash.setOnClickListener {
            if (isTorch){
                isTorch = false
                img_flash.setImageResource(R.drawable.flash_off)
            }else{
                isTorch = true
                img_flash.setImageResource(R.drawable.flash_on)
            }
            barcode_view.setTorch(isTorch)
        }
    }

    override fun onResume() {
        barcode_view.resume()
        super.onResume()
    }

    override fun onPause() {
        barcode_view.pause()
        super.onPause()
    }

    private val callback = object : BarcodeCallback{
        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

        }

        override fun barcodeResult(result: BarcodeResult?) {
            if (result!!.text != null){
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString(KEY_RESULT, result.text.toString())
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                val vibrator = application.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)
                finish()
            }
        }

    }
}
