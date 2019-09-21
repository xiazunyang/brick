package com.numeron.wan.util


sealed class DialogButton(val text: String, val onClick: () -> Unit)


class PositiveButton(text: String = "确定", onClick: () -> Unit = {}) : DialogButton(text, onClick)


class NegativeButton(text: String = "取消", onClick: () -> Unit = {}) : DialogButton("取消", onClick)


class NeutralButton(text: String, onClick: () -> Unit) : DialogButton(text, onClick)