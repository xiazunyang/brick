简化在申请权限时的代码  
简化在Activity之间传递参数和接收参数的代码
---

* 申请摄像头权限、调用其它Activity并接收返回结果的示例：

        //先申请权限
        requestPermissions(Manifest.permission.CAMERA) {
            //把照片保存到不需要权限就能使用的缓存目录下
            val photoPath = externalCacheDir ?: cacheDir
            if (!photoPath.exists()) photoPath.mkdirs()
            //分配一个尽量不重复的名称
            val photoFile = File(photoPath, "${System.currentTimeMillis()}.jpg")
            if (photoFile.exists()) photoFile.delete()
            //转换为uri
            val outputUri = photoFile.toUri()
            //装载Intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            //启动Activity并接收结果
            startActivityForResult(intent) {
                //显示图片
                imageView.setImageURI(outputUri)
            }.setOnCanceledCallback {
                toast("取消了拍照")
            }
            //设置申请权限失败时的回调
        }.setOnDeniedCallback {
            AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setCancelable(false)
                    .setMessage("摄像头权限被拒绝，无法调用相机功能。")
                    .setPositiveButton("我知道了") { d, _ ->
                        d.dismiss()
                    }
                    .setNegativeButton("再次申请") { d, _ ->
                        //直接调用此方法来继续发起申请权限操作
                        onClick(null)
                        d.dismiss()
                    }
                    .show()
        }