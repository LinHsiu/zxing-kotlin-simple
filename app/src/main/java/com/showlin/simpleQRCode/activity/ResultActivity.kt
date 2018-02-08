package com.showlin.simpleQRCode.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.ArrayMap
import android.view.MenuItem
import android.view.View
import com.showlin.simpleQRCode.R
import com.showlin.simpleQRCode.Utility.KEY_RESULT
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val bundle = intent.extras

        supportActionBar!!.title = getString(R.string.result)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pareInfo(bundle.getString(KEY_RESULT))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pareInfo(resultString: String){
        when {
            resultString.indexOf("WIFI:") == 0 -> // WIFI
                getWIFI(resultString)
            resultString.indexOf("http") == 0 -> // Website
                getWebsite(resultString)
            resultString.indexOf("smsto:") == 0 -> //SMS
                getSMS(resultString)
            resultString.indexOf("geo:") == 0 -> //GEO
                getGEO(resultString)
            resultString.indexOf("tel:") == 0 -> //TEL
                getTEL(resultString)
            resultString.indexOf("mailto:") == 0 -> //MAIL
                getMail(resultString)
//            resultString.indexOf("MECARD:") == 0 -> {
//                //MECARD
//            }
//            resultString.indexOf("BEGIN:VCARD") == 0 -> //VCARD
//                getVcard()
//            resultString.indexOf("BEGIN:VEVENT") == 0 -> {
//                //VEVENT
//            }
            else -> //TEXT
                getTextType(resultString)
        }
    }

    private fun getWIFI(stringResult : String){
        ll_wifi.visibility = View.VISIBLE
        text_wifi_string.text = stringResult

        val subString = stringResult.substring(5)
        val strings = subString.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val map = ArrayMap<String, String>()
        strings.indices
                .map { i -> strings[i].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() }
                .forEach { map[it[0]] = it[1] }
        btn_wifi.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.hi))
                    .setMessage(String.format(getString(R.string.wifi_connect), map["S"]))
                    .setNeutralButton(getString(R.string.ok), { dialog, _ ->
                        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val configuration = WifiConfiguration()
                        configuration.SSID = "\"" + map["S"] + "\""
                        configuration.status = WifiConfiguration.Status.ENABLED

                        if (map.containsKey("T")) {
                            if (map["T"].equals("WPA")) {
                                configuration.preSharedKey = "\"" + map["P"] + "\""
                                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                                configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                                configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                                configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                                configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                            } else if (map["T"].equals("WEP")) {
                                configuration.wepKeys[0] = "\"" + map["P"] + "\""
                                configuration.wepTxKeyIndex = 0
                                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                            }
                        } else {
                            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                        }

                        val netID = wifiManager.addNetwork(configuration)
                        wifiManager.enableNetwork(netID, true)
                        wifiManager.reconnect()

                        dialog.dismiss()
                    })
                    .setPositiveButton(getString(R.string.cancel), { dialog, _ -> dialog.dismiss() })
                    .create()
            alertDialog.show()
        }
    }

    private fun getWebsite(resultString: String) {
        ll_url.visibility = View.VISIBLE
        text_url_string.text = resultString
        btn_url.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(resultString)
            startActivity(intent)
        }
    }

    private fun getSMS(resultString: String) {
        ll_sms.visibility = View.VISIBLE
        text_sms_string.text = resultString
        val strResult = resultString.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        btn_sms.setOnClickListener {
            val uri = Uri.fromParts("sms", strResult[1], null)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", strResult[2])
            startActivity(intent)
        }
    }

    private fun getGEO(resultString: String) {
        ll_geo.visibility = View.VISIBLE
        text_geo_string.text = resultString
        btn_geo.setOnClickListener {
            val uri = Uri.parse(resultString)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun getTEL(resultString: String) {
        ll_tel.visibility = View.VISIBLE
        text_tel_string.text = resultString
        btn_tel.setOnClickListener {
            val uri = Uri.parse(resultString)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun getMail(resultString: String) {
        ll_email.visibility = View.VISIBLE
        text_email_string.text = resultString

        btn_email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(resultString)
            startActivity(intent)
        }
    }

    private fun getTextType(resultString: String) {
        ll_text.visibility = View.VISIBLE
        text_text_string.text = resultString
    }

    private fun getVcard() {
//        var contentProviderOperation = ContentProviderOperation.newInsert(Content)
    }



}
