package com.example.jobschedular

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jobschedular.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStartJob.setOnClickListener {
            val componentName = ComponentName(
                this@MainActivity,
                MyJobService::class.java
            )
            val jobInfoBuilder = JobInfo.Builder(1, componentName)
            //                jobInfoBuilder.setRequiresCharging(true);
            //                jobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            //                jobInfoBuilder.setPeriodic(15 * 60 * 1000);
            //                jobInfoBuilder.setPersisted(true);
            val jobInfo = jobInfoBuilder.build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = jobScheduler.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("TAG", "Job scheduled")
            } else {
                Log.d("TAG", "Job failed.")
            }
        }

        binding.btnStopJob.setOnClickListener {
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(1)
            Log.d("TAG", "Job Cancelled")
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("job")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra("key")
            binding.textView.text = result
        }
    }
}