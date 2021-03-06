package com.example.moviesapp.ui.presentation.movies.view.details

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.CalendarView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.moviesapp.R
import com.example.moviesapp.application.MovieApp
import com.example.moviesapp.model.entities.movies.details.ResponseMovieDetail
import com.example.moviesapp.ui.presentation.movies.viewmodel.CalendarViewModel
import java.util.*
import android.util.Log


class CalendarView(private val movie: ResponseMovieDetail?=null) : DialogFragment() {

    private var calendar: CalendarView? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var data : ResponseMovieDetail?= null

    private val viewModel : CalendarViewModel by lazy{
        (requireContext().applicationContext as MovieApp).myComponent.getCalendarViewModel(fragment=this@CalendarView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.fragment_calendar, null, false)
        calendar = view?.findViewById(R.id.calendar_view)

        calendarSetDate()

        calendar?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            run {
                arguments = Bundle().apply{
                    putInt(SAVE_YEAR_DATE,year)
                    putInt(SAVE_DAY_DATE,dayOfMonth)
                    putInt(SAVE_MONTH_DATE,month)
                }
            }
        }

        val builder = context?.let { AlertDialog.Builder(it) }

        builder?.let {
            it.setTitle(getString(R.string.select_movie))
                .setView(view)
                .setPositiveButton(
                    getString(R.string.yes_dialog)
                ) { dialog, _ ->
                    run {
                        checkPermissionsCalendar()
                        dialog.cancel()
                    }
                }
                .setNegativeButton(getString(R.string.no_dialog)) { dialog, _ -> dialog.cancel() }
        }

        try{
            viewModel.startView(data=movie!!)
        }catch(e : NullPointerException){
            e.printStackTrace()
        }
        Log.d("DATA", movie?.title.toString())
        viewModel.movie.observe(this,{data=it})

        return builder?.create()!!
    }



    private fun calendarSetDate(){
        val month = arguments?.getInt(SAVE_MONTH_DATE)
        val day = arguments?.getInt(SAVE_DAY_DATE)
        val year = arguments?.getInt(SAVE_YEAR_DATE)

        calendar?.date =  getDate(year=year,month=month,day=day).timeInMillis
    }

    private fun getDate(year : Int? , month : Int? , day : Int?) : Calendar {
        val date = Calendar.getInstance()
        val todayDate = Calendar.getInstance()
        date.set(
            year ?:todayDate.get(Calendar.YEAR),
            month ?: todayDate.get(Calendar.MONTH),
            day ?: todayDate.get(Calendar.DAY_OF_MONTH)
        )

        return date
    }



    private fun checkPermissionsCalendar() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_CALENDAR)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CALENDAR)
                        == PackageManager.PERMISSION_GRANTED-> addCalendarEvent()

                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR)->
                    showPermissionExplanation()

                else -> requestPermissions()

            }
        }
    }

    private fun showPermissionExplanation() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.access_text))
                .setPositiveButton(getString(R.string.ok_dialog)) { dialog, _ ->
                    requestPermissions()
                    addCalendarEvent()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.cancel_dialog)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun requestPermissions() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
        }
    }

    private fun addCalendarEvent() {
        Log.d("DATA",data?.title.toString())
        calendar?.let{

            val month = arguments?.getInt(SAVE_MONTH_DATE)
            val day = arguments?.getInt(SAVE_DAY_DATE)
            val year = arguments?.getInt(SAVE_YEAR_DATE)

            val date = getDate(year=year,month=month,day=day).timeInMillis

            val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, data?.title)
                .putExtra(CalendarContract.Events.DESCRIPTION,data?.overview)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Home")
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, date)

            startActivity(insertCalendarIntent)
        }

    }

    companion object {
        private const val SAVE_MONTH_DATE = "SAVE_MONTH_DATE"
        private const val SAVE_YEAR_DATE = "SAVE_YEAR_DATE"
        private const val SAVE_DAY_DATE = "SAVE_DAY_DATE"
    }
}