package com.showlin.zxing_kotlin_simple

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_capture.setOnClickListener{
            Utility.scan(this)
        }

        btn_generate.setOnClickListener {
            val strGenerate = edit_text.text.toString()
            if (strGenerate.isNotEmpty()){
                val bitmap : Bitmap = Utility.generateQR(strGenerate)!!
                img_qr_code.setImageBitmap(bitmap)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult.contents == null){
            // no result
        }else{
            val result = Utility.pareInfo(intentResult.contents)
            text_result.text = result
        }
    }


}
