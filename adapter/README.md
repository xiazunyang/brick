### RecyclerView的辅助工具

* LiteAdapter
    * 默认情况下，长度不变、布局单一的RecyclerView适配器，继承此类仅需实现onBindViewHolder方法即可。
    * 如果列表长度会改变，请重写getItemCount()方法。
    * 如果有多种布局，请重写getItemViewType()方法，并将布局ID作为返回值。
    
* BindingAdapter
    * 使用DataBinding的LiteAdapter。
    * 接收一个文件名通过layout的名字生成的、并且继承自ViewDataBinding的抽象类型作为泛型参数。
    * ViewHolder有binding成员，用于绑定数据和刷新数据等操作。
    
* PagedAdapter
    * 使用Paging的LiteAdapter。
    
* PagedBindingAdapter
    * 使用Paging和DataBinding的Adapter。
    
* SpaceItemDecoration
    * 为RecyclerView的每个Item四周设置一个距离。
    
* DiffUtil
    * 当RecyclerView的列表发生变化时，使用此工具来对比差异，通过调用dispatchUpdatesTo()方法来处理动画
