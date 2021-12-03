package ru.fefu.activitytracker.views.main.fragments.activities

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.BaseFragment
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.MyActivityCardInfoBinding
import ru.fefu.activitytracker.model.MyActivity

class MyActivityInfo:
    BaseFragment<MyActivityCardInfoBinding>(R.layout.my_activity_card_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val data: MyActivity = requireArguments().get("Activity") as MyActivity

        binding.tbAction.title = data.name
        binding.tvMetric.text = data.metric
        binding.tvStartTimeValue.text = data.startTime
        binding.tvFinishTimeValue.text = data.finishTime
        binding.tvDate.text = data.finishDate

        binding.tbAction.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        binding.tbAction.setOnMenuItemClickListener {
            when (it.order) {
                MenuItemType.Delete.ordinal -> {
                    App.INSTANCE.database.activityDao().deleteById(data.id)
                    findNavController().popBackStack()
                }
                else -> {}
            }
            true
        }

        super.onViewCreated(view, savedInstanceState)
    }
}

internal enum class MenuItemType {
    Delete,
    Share
}