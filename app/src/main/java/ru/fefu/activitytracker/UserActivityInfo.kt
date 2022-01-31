package ru.fefu.activitytracker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.fefu.activitytracker.adapter.ListAdapter
import ru.fefu.activitytracker.data.UserActivityData
import ru.fefu.activitytracker.databinding.UserActivityDetailsBinding
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class UserActivityInfo : Fragment() {
    private var _binding: UserActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private var info: UserActivityData? = null

    companion object {
        fun newInstance(
            distance: String,
            activityType: String,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            user: String
        ) = UserActivityInfo().apply {
            arguments = bundleOf(
                "distance" to distance,
                "activity_type" to activityType,
                "start_date" to startDate,
                "end_date" to endDate,
                "user" to user
            )
        }
        const val tag = "user_info"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val distance = requireArguments().getString("distance") ?: "0"
        val activityType = requireArguments().getString("activity_type") ?: "0"
        val startDate = requireArguments().getSerializable("start_date") as LocalDateTime
        val endDate = requireArguments().getSerializable("end_date") as LocalDateTime
        val user = requireArguments().getString("user") ?: "0"
        info = UserActivityData(distance, activityType, startDate, endDate, user)
        binding.user.text = info?.user
        binding.distance.text = info?.distance
        val startTime =
            info?.startDate?.let { "%02d".format(it.hour) } + ":" + info?.startDate?.let {
                "%02d".format(
                    it.minute
                )
            }
        val endTime = info?.endDate?.let { "%02d".format(it.hour) } + ":" + info?.endDate?.let {
            "%02d".format(
                it.minute
            )
        }

        binding.startTime.text = startTime
        binding.finishTime.text = endTime

        val duration_ = Duration.between(info?.endDate, info?.startDate);
        var seconds: Long = abs(duration_.seconds)
        val hours = seconds / 3600
        seconds -= hours * 3600
        val minutes = seconds / 60
        if (hours > 0) binding.duration.text = "%d ч %d мин".format(hours, minutes)
        else binding.duration.text = "%d мин".format(minutes)



        if (LocalDateTime.now().equals(info?.endDate)) {
            binding.date.text =
                Duration.between(info?.endDate, LocalDateTime.now()).toHours().toString() +
                        ListAdapter.getNoun(
                            Duration.between(
                                info?.endDate,
                                LocalDateTime.now()
                            ).toHours(), " час", " часа", " часов"
                        ) +
                        " назад"
        } else binding.date.text = info?.endDate?.dayOfMonth.toString() + '.' +
                info?.endDate?.monthValue.toString() + '.' + info?.endDate?.year.toString()
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}