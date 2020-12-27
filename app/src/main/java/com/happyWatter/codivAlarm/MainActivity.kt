package com.happyWatter.codivAlarm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {

    //    배너광고, 전면광고 , 버튼 지정
    lateinit var mAdView : AdView
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(object : OnSuccessListener<InstanceIdResult> {
                override fun onSuccess(instanceIdResult: InstanceIdResult) {
                    val token = instanceIdResult.token //Token
                    sendGetRequest(token)

                }
            })


        val myWebView = findViewById<View>(R.id.webview) as WebView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)

        }

        val settings = myWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true

        myWebView.loadUrl("http://35.202.26.74:8184/")


        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)

        mInterstitialAd.adUnitId = "ca-app-pub-5821312827792329/1683876903"
//        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.show()

    }


    fun sendGetRequest(token: String) {
        // URL을 만들어 주고
//        val url = URL("http://localhost:9080/token")
        val url = URL("http://35.202.26.74:9080/token")

        val jsonObject = JSONObject();
        jsonObject.put("token", token);

        // OkHttp Request 를 만들어준다.
        val request = Request.Builder()
            .url(url)
            .post(
                jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            )
            .build()

        // 클라이언트 생성
        val client = OkHttpClient()

        // 요청 전송
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "요청 완료")
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 실패 ")
            }

        })




    }
}

