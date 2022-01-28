package ru.fefu.activitytracker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.fefu.activitytracker.adapter.ListAdapter
import ru.fefu.activitytracker.data.ActivityData
import ru.fefu.activitytracker.databinding.MyActivityDetailsBinding
import java.time.Duration
import java.time.LocalDate

class MyActivityInfo(private val info: ActivityData) : Fragment() {
    private var _binding: MyActivityDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(info: ActivityData): MyActivityInfo {
            return MyActivityInfo(info)
        }

        const val tag = "my_info"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.distance.text = info.distance
        val startTime =
            "%02d".format(info.startDate.hour) + ":" + "%02d".format(info.startDate.minute)
        val endTime = "%02d".format(info.endDate.hour) + ":" + "%02d".format(info.endDate.minute)

        binding.startTime.text = startTime
        binding.finishTime.text = endTime

        if (LocalDate.now().equals(info.endDate.toLocalDate())) {
            binding.date.text =
                Duration.between(info.endDate, LocalDate.now()).toHours().toString() +
                        ListAdapter.getNoun(
                            Duration.between(
                                info.endDate,
                                LocalDate.now()
                            ).toHours(), " час", " часа", " часов"
                        ) +
                        " назад"
        } else binding.date.text = info.endDate.dayOfMonth.toString() + '.' +
                info.endDate.monthValue.toString() + '.' + info.endDate.year.toString()

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}