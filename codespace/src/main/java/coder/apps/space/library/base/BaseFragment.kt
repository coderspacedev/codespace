package coder.apps.space.library.base

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.viewbinding.*
import coder.apps.space.library.helper.*

abstract class BaseFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : Fragment() {

    var tinyDB: TinyDB? = null
    var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tinyDB = TinyDB(requireContext())
        create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingFactory(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            it.viewCreated()
            it.initListeners()
            it.initView()
        }
    }

    abstract fun B.viewCreated()
    abstract fun B.initListeners()
    abstract fun B.initView()
    abstract fun create()
}