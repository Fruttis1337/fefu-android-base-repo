package ru.fefu.activitytracker

import ru.fefu.activitytracker.ui.profile.ProfileFragment
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.fefu.activitytracker.databinding.ActivityTrackerBinding

data class FragmentInfo(
    val buttonId: Int,
    val newInstance: () -> Fragment,
    val tag: String,
)

class TrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackerBinding

    private val fragments = listOf<FragmentInfo>(
        FragmentInfo(R.id.action_activity_tracker, ActivityTabs::newInstance, ActivityTabs.tag),
        FragmentInfo(R.id.action_profile, ProfileFragment::newInstance, ProfileFragment.tag)
    )

    private var activeFragment = fragments[0]
    private var notActiveFragment = fragments[1]

    private fun replaceFragment(buttonId: Int) {
        if (activeFragment.buttonId == buttonId) {
            return
        }

        val active = supportFragmentManager.findFragmentByTag(activeFragment.tag)
        val notActive = supportFragmentManager.findFragmentByTag(notActiveFragment.tag)

        if (active != null) {
            supportFragmentManager.beginTransaction().apply {
                hide(active)
                commit()
            }
        }
        if (notActive != null) {
            supportFragmentManager.beginTransaction().apply {
                show(notActive)
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.fragment_container_view,
                    notActiveFragment.newInstance(),
                    notActiveFragment.tag
                )
                commit()
            }
        }
        activeFragment = notActiveFragment.also { notActiveFragment = activeFragment }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.fragment_container_view,
                    ActivityTabs.newInstance(),
                    ActivityTabs.tag,
                )
                commit()
            }
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            replaceFragment(it.itemId)
            true
        }
    }

    override fun onBackPressed() {
        val active = supportFragmentManager.fragments.firstOrNull { !it.isHidden }!!
        val childManager = active.childFragmentManager
        when {
            binding.bottomNavigationView.visibility == View.GONE && childManager.findFragmentByTag(
                NewActivityFragment.tag
            )?.isVisible == true -> binding.bottomNavigationView.visibility = View.VISIBLE
        }
        when {
            supportFragmentManager.backStackEntryCount != 0 -> {
                supportFragmentManager.popBackStack()
            }
            childManager.backStackEntryCount != 0 -> {
                childManager.popBackStack()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}