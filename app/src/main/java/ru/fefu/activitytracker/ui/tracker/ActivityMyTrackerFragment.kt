package ru.fefu.activitytracker.ui.tracker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.fefu.activitytracker.*
import ru.fefu.activitytracker.adapter.ListAdapter
import ru.fefu.activitytracker.data.ActivityData
import ru.fefu.activitytracker.data.DateData
import ru.fefu.activitytracker.data.enum.ActivitiesEnum
import ru.fefu.activitytracker.databinding.ActivityFragmentTrackingMyBinding
import java.nio.BufferUnderflowException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ActivityMyTrackerFragment : Fragment(R.layout.activity_fragment_tracking_my) {
    private var _binding: ActivityFragmentTrackingMyBinding? = null
    private val binding get() = _binding!!
    private val activities = mutableListOf<ActivityData>()
    private val data_activities = mutableListOf<Any>()

    private val map = mapOf(
        1 to "Январь", 2 to "Февраль", 3 to "Март",
        4 to "Апрель", 5 to "Май", 6 to "Июнь",
        7 to "Июль", 8 to "Август", 9 to "Сентябрь",
        10 to "Октябрь", 11 to "Ноябрь", 12 to "Декабрь"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityFragmentTrackingMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fill_date(activities: List<ActivityData>) {
        val cur = LocalDateTime.now()
        var date = DateData("")
        for (activity in activities) {
            when {
                cur.year == activity.endDate.year &&
                        cur.monthValue == activity.endDate.monthValue &&
                        cur.dayOfMonth == activity.endDate.dayOfMonth -> {
                    if (date.Date != "Сегодня") {
                        date = DateData("Сегодня")
                        data_activities.add(date)
                    }
                }
                else -> {
                    if (date.Date != map[activity.endDate.monthValue] + ' ' + activity.endDate.year.toString() + " года") {
                        date =
                            DateData(map[activity.endDate.monthValue] + ' ' + activity.endDate.year.toString() + " года")
                        data_activities.add(date)
                    }
                }
            }
            data_activities.add(activity)
        }
    }

    private val adapter = ListAdapter(data_activities)

    private fun changeFragment(position: Int) {
        if (position in data_activities.indices) {
            val activityData = data_activities[position] as ActivityData
            val fragment =
                MyActivityInfo.newInstance(
                    activityData.distance,
                    activityData.activityType,
                    activityData.startDate,
                    activityData.endDate,
                )
            val manager =
                activity?.supportFragmentManager?.findFragmentByTag(ActivityTabs.tag)?.childFragmentManager
            manager?.beginTransaction()?.apply {
                manager.fragments.forEach(::hide)
                add(
                    R.id.activity_fragment_container,
                    fragment,
                    "my_info",
                )
                addToBackStack(null)
                commit()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.INSTANCE.db.activityDao().getAll().observe(viewLifecycleOwner) {
            activities.clear()
            data_activities.clear()
            for (activity in it) {
                val startDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(activity.dateStart),
                    ZoneId.systemDefault()
                )
                val endDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(activity.dateEnd),
                    ZoneId.systemDefault()
                )
                val type = ActivitiesEnum.values()[activity.type].type
                val distance = (1..20).random().toString() + " км"
                activities.add(ActivityData(distance, type, startDate, endDate))
            }
            fill_date(activities)
            adapter.notifyDataSetChanged()
        }

        val recycleView = binding.recyclerView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter
        adapter.setItemClickListener { changeFragment(it) }
        binding.startNewActivity.setOnClickListener {
            val manager =
                activity?.supportFragmentManager?.findFragmentByTag(ActivityTabs.tag)?.childFragmentManager
            val navbar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            navbar?.visibility = View.GONE
            manager?.beginTransaction()?.apply {
                manager.fragments.forEach(::hide)
                add(
                    R.id.activity_fragment_container,
                    NewActivityFragment(),
                    NewActivityFragment.tag
                )
                addToBackStack(null)
                commit()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}