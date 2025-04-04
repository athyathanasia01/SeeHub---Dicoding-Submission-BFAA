package com.dicoding.seehub.ui

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.dicoding.seehub.R
import com.dicoding.seehub.data.response.DetailResponse
import com.dicoding.seehub.databinding.FragmentDialogDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DialogDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogDetailFragment : DialogFragment() {

    private var _binding : FragmentDialogDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USER_DETAIL = "detail_user"

        fun newInstance(user : DetailResponse) : DialogDetailFragment {
            val fragment = DialogDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_USER_DETAIL, user)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)

        val user = arguments?.getParcelable<DetailResponse>(EXTRA_USER_DETAIL) as DetailResponse

        binding.apply {
            //text name
            tvName.setText(user.name)
            //text username
            tvUsername.setText(user.login)
            //text id
            tvId.setText(user.id.toString())
            //text node id
            tvNodeId.apply {
                setText(user.nodeId)
                ellipsize = null
                maxLines = Int.MAX_VALUE
            }
            //text email
            tvEmail.apply {
                setText(user.email)
                ellipsize = null
                maxLines = Int.MAX_VALUE
            }
            //text company
            if (user.company == null) {
                tvCompany.apply {
                    setText(String.format(requireActivity().getString(R.string.null_)))
                    setTextAppearance(R.style.TextItalic)
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.link))
                }
            } else {
                tvCompany.apply {
                    setText(user.company)
                    maxLines = Int.MAX_VALUE
                    ellipsize = null
                }
            }
            //text location
            tvLocation.setText(user.location)
            //text twitter
            if (user.twitterUsername == null) {
                tvTwitter.apply {
                    setText(String.format(requireActivity().getString(R.string.null_)))
                    setTextAppearance(R.style.TextItalic)
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.link))
                }
            } else {
                tvTwitter.setText(user.twitterUsername)
            }
            //text public repo and gist
            tvPubRepo.setText(user.publicRepos.toString())
            tvPubGist.setText(user.publicGists.toString())
            //text create account
            tvCreate.apply {
                setText(user.createdAt)
                maxLines = Int.MAX_VALUE
                ellipsize = TextUtils.TruncateAt.END
            }
            //text update account
            tvUpdate.apply {
                setText(user.updatedAt)
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
            }
            //text bio
            if (user.bio == null) {
                tvBio.setText("...")
            } else {
                tvBio.setText(user.bio)
            }
        }

        binding.btnClose.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.dimAmount = 0.7f

        val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        dialog?.window?.setLayout(
            Resources.getSystem().displayMetrics.widthPixels - (2 * margin),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}