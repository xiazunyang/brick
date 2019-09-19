package com.numeron.wan.abs

import com.numeron.frame.base.IModel
import com.numeron.frame.mvvm.AbstractViewModel
import com.numeron.frame.mvvm.IView


abstract class AbsActionActivity : AbsMvvmActivity<EmptyViewModel>(), EmptyView


interface EmptyView : IView<EmptyViewModel>


class EmptyViewModel : AbstractViewModel<EmptyView, EmptyModel>()


class EmptyModel : IModel