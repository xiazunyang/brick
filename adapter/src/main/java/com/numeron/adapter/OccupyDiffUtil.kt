package com.numeron.adapter

/**
 * 当RecyclerView中有占位的ItemView时，使用此工具来比较列表差异、处理动画
 */
class OccupyDiffUtil<T>(oldList: List<T>, newList: List<T>, private val occupy: Int) : DiffUtil<T>(oldList, newList) {

    override fun getOldListSize() = oldList.size + occupy

    override fun getNewListSize() = newList.size + occupy

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val modifiedOldItemPosition = oldItemPosition - occupy
        val modifiedNewItemPosition = newItemPosition - occupy

        return if (oldItemPosition < occupy || newItemPosition < occupy) {
            oldItemPosition == newItemPosition
        } else {
            super.areItemsTheSame(modifiedOldItemPosition, modifiedNewItemPosition)
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val modifiedOldItemPosition = oldItemPosition - occupy
        val modifiedNewItemPosition = newItemPosition - occupy

        return if (oldItemPosition < occupy || newItemPosition < occupy) {
            oldItemPosition == newItemPosition
        } else {
            super.areContentsTheSame(modifiedOldItemPosition, modifiedNewItemPosition)
        }

    }

}