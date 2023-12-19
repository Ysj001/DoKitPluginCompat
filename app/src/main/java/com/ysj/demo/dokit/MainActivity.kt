package com.ysj.demo.dokit

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysj.demo.dokit.databinding.ActivityMainBinding
import com.ysj.demo.dokit.databinding.ItemImageBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 演示。
 *
 * @author Ysj
 * Create time: 2023/8/31
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val okhttp by lazy(LazyThreadSafetyMode.NONE) {
        OkHttpClient
            .Builder()
            .callTimeout(1000L, TimeUnit.MILLISECONDS)
            .connectTimeout(1000L, TimeUnit.MILLISECONDS)
            .readTimeout(1000L, TimeUnit.MILLISECONDS)
            .writeTimeout(1000L, TimeUnit.MILLISECONDS)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        val imageAdapter = ImageAdapter()
        vb.list.adapter = imageAdapter
        vb.btnSlowMethod.setOnClickListener {
            SystemClock.sleep(800)
        }
        vb.btnHttp.setOnClickListener {
            okhttp
                .newCall(Request.Builder().get().url("https://www.baidu.com/").build())
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "onFailure", e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.close()
                        Log.i(TAG, "onResponse.")
                    }
                })
        }
        vb.btnBigImage.setOnClickListener {
            imageAdapter.list.addAll(listOf(
                "https://liblibai-online.vibrou.com/web/image/6b834a133e564ae3d0ca2fff54a6421868eede92c9a9215a53c503b05bfe376d.jpg",
                "https://liblibai-online.vibrou.com/web/image/d257d414d2f13757009fe57ec09ac75f37fb9560c841024bbf3b3035f6557f36.jpg",
                "https://liblibai-online.vibrou.com/web/image/a85472cf25e9b8ae70d1034b995e9831675cc9069b07f890203e3511914dc240.png",
                "https://liblibai-online.vibrou.com/web/image/aa7920631370071dabc77627c8d5d22982ccb3dfd663c608c48c370e59fae388.png",
                "https://liblibai-online.vibrou.com/web/image/5e290cdf6a155e5e120d989fddffa6232b849053ddcffd0ee74efd8ed3c31300.png",
                "https://liblibai-online.vibrou.com/operate/410eb68c-5f33-400d-ce69-debd4a3b1c1a.png",
                "https://liblibai-online.vibrou.com/web/image/d6117bccce172f41fbb047501d20c2f34d9cb7f9b44acd101826d3cad83b6acf.png",
                "https://liblibai-online.vibrou.com/web/image/8810a2fbef2fb080641f41cb34d1af5a7bf7b530b4d055d8b9cd97b67fdcdcce.png",
            ))
            imageAdapter.notifyDataSetChanged()
        }
    }

    private inner class ImageAdapter : RecyclerView.Adapter<ImageAdapter.VH>() {

        val list = ArrayList<String>()

        inner class VH(val vb: ItemImageBinding) : RecyclerView.ViewHolder(vb.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            Glide
                .with(this@MainActivity)
                .load(list[position])
                .into(holder.vb.root)
        }
    }
}