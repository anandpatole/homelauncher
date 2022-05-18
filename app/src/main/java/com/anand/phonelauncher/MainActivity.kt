package com.anand.phonelauncher

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anand.launcherlib.ApplicatonList
import com.anand.launcherlib.interfaces.NewAppListener
import com.anand.launcherlib.interfaces.UninstalledAppListener
import com.anand.launcherlib.model.ApplicationInfo
import com.anand.phonelauncher.adapter.AppAdapter


class MainActivity : AppCompatActivity() ,AppAdapter.RecyclerClickInterface, NewAppListener,UninstalledAppListener {
    //    lateinit var  binding:ActivityMainBinding
    lateinit var appAdpater:AppAdapter
    lateinit var recycler:RecyclerView
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding=  DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(R.layout.activity_main)


        intiViews()
        setListerners()

    }

    private fun setListerners() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filter(newText.toString())
                return false
            }

        })
    }



    private fun filter(toString: String) {
        appAdpater?.filter?.filter(toString)
    }

    private fun intiViews() {
//        binding.appRecycler.apply {
//            layoutManager=GridLayoutManager(this@MainActivity,5)
//            isNestedScrollingEnabled=false
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerReceiver(
            ApplicatonList(),
            ApplicatonList.intentFilter
        )
        searchView=findViewById(R.id.searchView)
        recycler=findViewById(R.id.app_recycler)
        recycler.layoutManager=GridLayoutManager(this, 3)
        recycler.isNestedScrollingEnabled=false
        var list=  ApplicatonList.getApplicationsInfo(applicationContext)
        appAdpater= AppAdapter(
            context = this,
            arrayList = list as ArrayList<ApplicationInfo>?,
            listerns = this
        )
        recycler.adapter=appAdpater
        ApplicatonList.registerListners(this, this)




    }

    override fun onClick(pojo: ApplicationInfo?) {
        var intent: Intent? = getPackageManager().getLaunchIntentForPackage(pojo?.packageName.toString())
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {

        }
    }

    override fun onLongCick(pojo: ApplicationInfo?) {


        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:${pojo?.packageName}")
        startActivity(intent)
    }

    override fun newAppListener() {
        Toast.makeText(this, "App installed", Toast.LENGTH_LONG).show()
        var list=  ApplicatonList.getApplicationsInfo(applicationContext)
        appAdpater= AppAdapter(
            context = this,
            arrayList = list as ArrayList<ApplicationInfo>?,
            listerns = this
        )
        recycler.adapter=appAdpater
    }

    override fun uninstallAppListener() {
        Toast.makeText(this, "App Uinstalled", Toast.LENGTH_LONG).show()
        var list=  ApplicatonList.getApplicationsInfo(applicationContext)
        appAdpater= AppAdapter(
            context = this,
            arrayList = list as ArrayList<ApplicationInfo>?,
            listerns = this
        )
        recycler.adapter=appAdpater
    }
}