package com.example.shubhammishra.fetchdatafromwebsite

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shubhammishra.fetchdatafromwebsite.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSearch.setOnClickListener(View.OnClickListener {
            val name=etUrl.text.toString()
            val url="https://api.github.com/search/users?q=$name"
            try {
                networkCall(url)
            }
            catch (e:IOException)
            {
                e.printStackTrace()
            }
            //GetData().execute(url)
        })
    }
    fun networkCall(url:String):Unit
    {
         val client=OkHttpClient()
         val request=Request.Builder().url(url).build()
          showProgress.visibility=View.VISIBLE
        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(this@MainActivity,"Failed to load url request",Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call?, response: Response?) {
                val result= response!!.body()!!.string()
                val users=parseJason(result)
                val gitHubAdapter=GitHubAdapter(users)
                this@MainActivity.runOnUiThread(object:Runnable{
                    override fun run() {
                        recycleView.layoutManager=LinearLayoutManager(this@MainActivity)
                        recycleView.adapter=gitHubAdapter
                        Toast.makeText(this@MainActivity,"${users.size} results found!",Toast.LENGTH_SHORT).show()
                        showProgress.visibility=View.GONE
                    }
                })
            }
        })

    }
    inner class GetData:AsyncTask<String,Unit,String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress.visibility=View.VISIBLE
            showProgress.setProgress(0)
            showProgress.max=2000
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val users=parseJason(result)
            val gitHubAdapter=GitHubAdapter(users)
            recycleView.layoutManager=LinearLayoutManager(this@MainActivity)
            recycleView.adapter=gitHubAdapter
            Toast.makeText(this@MainActivity,"${users.size} results found!",Toast.LENGTH_SHORT).show()
            showProgress.visibility=View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var bf:String?=""
            try {
              val url=URL(params[0])
              val httpURLConnection=url.openConnection()as HttpURLConnection
                /*val inputStreamReader=InputStreamReader(httpURLConnection.inputStream)
              var buf=BufferedReader(inputStreamReader)
              var sb=StringBuilder()

              while(bf!=null)
              {
                  sb.append(bf)
                  bf=buf.readLine()
              }*/
               val scanner=Scanner(httpURLConnection.inputStream)
                scanner.useDelimiter("\\A")
                if(scanner.hasNext())
                {
                    bf=scanner.next()
                }
                return bf
          }
          catch (e:IOException)
          {
              Log.e("Exception","Url is invalid")
          }
            return "Failed to Load"
        }
    }
    fun parseJason(string: String?): ArrayList<GitHubUser> {
        var userArray=ArrayList<GitHubUser>()
        try {
            val jsonObject=JSONObject(string)
            val items=jsonObject.getJSONArray("items")
            for(i in 0..items.length()-1)
            {
                val item=items.getJSONObject(i)
                val id=item.getInt("id")
                val login=item.getString("login")
                val avtar_url=item.getString("avatar_url")
                val html_url=item.getString("html_url")
                val score=item.getDouble("score")
                val gitHubUser=GitHubUser(login,id,avtar_url,html_url,score)
                userArray.add(gitHubUser)
            }
        }
        catch (e:JSONException)
        {
            Log.e("Error","Exception in String passed in parameter")
        }
        return userArray
    }
}
