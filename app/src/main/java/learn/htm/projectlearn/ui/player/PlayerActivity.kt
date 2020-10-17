package learn.htm.projectlearn.ui.player

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseActivity
import learn.htm.projectlearn.databinding.ActivityPlayerBinding

@AndroidEntryPoint
class PlayerActivity : BaseActivity<ActivityPlayerBinding, PlayerViewModel>() {
    override val viewModel: PlayerViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.activity_player
}