package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import java.io.IOException
import androidx.core.widget.NestedScrollView

import android.widget.ProgressBar
import android.view.View
import android.widget.Toast


import okhttp3.Response
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var recyclerview : RecyclerView
    private val client = OkHttpClient()
    lateinit var loadingpb : ProgressBar
    lateinit var nestedSV : NestedScrollView
    var page = 0
    var limit = 2
    val data = ArrayList<DataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        loadingpb = findViewById(R.id.progressbar)
        nestedSV = findViewById(R.id.idNestedSV)
        run("https://www.onlinekaka.com/serviceclient/index.php/callback/nearByMerchantV2?page=5&q=biryani&lat=26.846693664076216&lon=80.9461659193039&order_by=restaurant_name&api_version=v2&search_type=item",page,limit)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)



        with(nestedSV) {
            this!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++
                    loadingpb!!.visibility = View.GONE
                    run("https://www.onlinekaka.com/serviceclient/index.php/callback/nearByMerchantV2?page=5&q=biryani&lat=26.846693664076216&lon=80.9461659193039&order_by=restaurant_name&api_version=v2&search_type=item",page,limit)

                }
            })
        }
    }

    fun run(url: String,page: Int, limit: Int) {
        if (page > limit) {
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show()

            loadingpb!!.visibility = View.GONE
            return
        }
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                println(response.body()?.string())
                var response_string = response.body()?.string()
                var jsonobject:JSONObject = JSONObject(response_string)
                var merchant_info = jsonobject.getJSONObject("merchant_info")
                var DishesArray = merchant_info.getJSONArray("dishes")

                if (DishesArray.length()!=0){
                    for (i in 0 until DishesArray.length()){
                        var Dishesobject : JSONObject = DishesArray.getJSONObject(i)

                        var Itemsarray = Dishesobject.getJSONArray("items")
                        for (j in 0 until Itemsarray.length()){
                            var Itemobject = Itemsarray.getJSONObject(j)
                            data.add(DataClass(Dishesobject.getString("pic"), Dishesobject.getString("restaurant_name"),Dishesobject.getString("ratings"),Itemobject.getString("item_name"),Itemobject.getString("price")))
                        }
                        var cuisine_info_array = Dishesobject.getJSONArray("cuisine_info")
                        for (k in 0 until cuisine_info_array.length()){
                            var cuisine_info_object = cuisine_info_array.getJSONObject(k)

                        }
                    }
                }
                val adapter = DataAdapter(data,this@MainActivity)
                runOnUiThread {
                    recyclerview.adapter = adapter
                }
            }
        })
    }
}