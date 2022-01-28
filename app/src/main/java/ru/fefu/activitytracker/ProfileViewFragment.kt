package ru.fefu.activitytracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.fefu.activitytracker.Retrofit.Result
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.activitytracker.data.UserModel
import ru.fefu.activitytracker.databinding.ProfileViewActivityBinding
import ru.fefu.activitytracker.ui.profile.ProfileFragment
import ru.fefu.activitytracker.viewmodels.ProfileViewModel

class ProfileViewFragment: Fragment() {
    private var _binding: ProfileViewActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    companion object {
        fun newInstance(): ProfileViewFragment{
            return ProfileViewFragment()
        }
        const val tag = "profile_view"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = ProfileViewActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.change.setOnClickListener{
            val manager = activity?.supportFragmentManager?.findFragmentByTag(ProfileFragment.tag)?.childFragmentManager
            manager?.beginTransaction()?.apply {
                manager.fragments.forEach(::hide)
                add (R.id.profile_fragment_container, ProfileChangeFragment.newInstance(), ProfileChangeFragment.tag)
                addToBackStack(null)
                commit()
            }
        }
        viewModel.logoutUser
            .onEach {
                if (it is Result.Success<Unit>) {
                    App.INSTANCE.sharedPrefs.edit().remove("token").apply()
                    val intent = Intent(requireActivity(), WelcomeScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else if (it is Result.Error<Unit>) {
                    Toast.makeText(requireContext(), it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)

        viewModel.profile
            .onEach {
                if (it is Result.Success<UserModel>) {
                    binding.login.editText?.setText(it.result.login)
                    binding.nickname.editText?.setText(it.result.name)
                }
                else if (it is Result.Error<UserModel>) {
                    Toast.makeText(requireContext(), it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)
        viewModel.getProfile()

        binding.buttonExit.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}