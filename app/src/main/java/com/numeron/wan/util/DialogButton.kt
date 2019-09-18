package com.numeron.wan.util


sealed class DialogButton(val text: String, val onClick: () -> Unit)


class PositiveButton(onClick: () -> Unit = {}) : DialogButton("确定", onClick)


class NegativeButton(onClick: () -> Unit = {}) : DialogButton("取消", onClick)


class NeutralButton(text: String, onClick: () -> Unit) : DialogButton(text, onClick)