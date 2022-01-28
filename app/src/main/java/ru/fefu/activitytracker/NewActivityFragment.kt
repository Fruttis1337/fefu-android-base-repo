package ru.fefu.activitytracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.activitytracker.Room.ActivityRoom
import ru.fefu.activitytracker.adapter.NewActivityListAdapter
import ru.fefu.activitytracker.data.NewActivityData
import ru.fefu.activitytracker.data.enum.ActivitiesEnum
import ru.fefu.activitytracker.databinding.NewActivityFragmentBinding

class NewActivityFragment: Fragment() {
    private var _binding: NewActivityFragmentBinding? = null
    private val binding get() = _binding!!
    private var activities = mutableListOf<NewActivityData>()
    private lateinit var adapter: NewActivityListAdapter

    companion object {
        fun newInstance(): NewActivityFragment {
            return NewActivityFragment()
        }
        const val tag = "new_activity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fillActivities()
        _binding = NewActivityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycleView = binding.newActivityList
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = adapter

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.buttonContinue.setOnClickListener {
            if (adapter.selected != -1) {
                val endDate = System.currentTimeMillis() - (0..604800000).random()
                val startDate = endDate - (600000..86400000).random()
                App.INSTANCE.db.activityDao().insert (
                    ActivityRoom (
                        0,
                        adapter.selected,
                        startDate,
                        endDate,
                        123.0,
                        131.0
                    )
                )
                val manager = activity?.supportFragmentManager?.findFragmentByTag(ActivityTabs.tag)?.childFragmentManager
                manager?.beginTransaction()?.apply {
                    manager.fragments.forEach(::hide)
                    add (
                        R.id.activity_fragment_container,
                        StartedActivityFragment.newInstance(),
                        StartedActivityFragment.tag,
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillActivities() {
        for(i in ActivitiesEnum.values()) {
            activities.add(NewActivityData(i.type, false))
        }
        adapter = NewActivityListAdapter(activities)
    }
}