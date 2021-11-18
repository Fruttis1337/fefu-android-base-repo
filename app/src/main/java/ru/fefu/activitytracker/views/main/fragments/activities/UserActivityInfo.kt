package ru.fefu.activitytracker.views.main.fragments.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.fefu.activitytracker.BaseFragment
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.UserActivityCardInfoBinding
import ru.fefu.activitytracker.models.UserActivity

class UserActivityInfo:
    BaseFragment<UserActivityCardInfoBinding>(R.layout.user_activity_card_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val data: UserActivity = requireArguments().get("Activity") as UserActivity

        binding.tbAction.title = data.name
        binding.tvMetric.text = data.metric
        binding.tvStartTimeValue.text = data.startTime
        binding.tvFinishTimeValue.text = data.finishTime
        binding.tvDate.text = data.finishDate
        binding.tvUserName.text = data.userName
        binding.tvComment.text = data.userComment

        binding.tbAction.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}