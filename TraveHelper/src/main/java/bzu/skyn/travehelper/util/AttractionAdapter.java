package bzu.skyn.travehelper.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import bzu.skyn.travehelper.R;
import bzu.skyn.travehelper.entity.AttractionEntity;

/**
 * Created by Dustray on 2017/10/28 0028.
 */

public class AttractionAdapter extends ArrayAdapter<AttractionEntity> {

    private int resourceId;
    private Context mContext;
    public AttractionAdapter(Context context, int textViewResourceId, List<AttractionEntity> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
    }

    @Override         //getView方法在每个子项被滚动到屏幕内的时候都会被调用，每次都将布局重新加载一边
    public View getView(int position, View convertView, ViewGroup parent) {//第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
        View view;
        ViewHolder viewHolder;                  //实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率
        if(convertView==null){
            viewHolder=new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(//convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建
                    resourceId, null);          // 得到子布局，非固定的，和子布局id有关
            viewHolder.aImage = (ImageView) view.findViewById(R.id.iv_att_image);//获取控件,只需要调用一遍，调用过后保存在ViewHolder中
            viewHolder.aName = (TextView) view.findViewById(R.id.tv_att_name);
            viewHolder.aAddress = (TextView) view.findViewById(R.id.tv_att_address);   //获取控件
            viewHolder.aAddressMore = (TextView) view.findViewById(R.id.tv_att_addressmore);
            viewHolder.aContent = (TextView) view.findViewById(R.id.tv_att_content);
            viewHolder.aLL = (LinearLayout) view.findViewById(R.id.ll_attr_position);
            view.setTag(viewHolder);
        }else{
            view=convertView;           //convertView不为空代表布局被加载过，只需要将convertView的值取出即可
            viewHolder=(ViewHolder) view.getTag();
        }

        AttractionEntity att = getItem(position);//实例指定位置的水果

        if(att.getImageUrl()!=null) {
//            asyncloadImage(viewHolder.aImage,att.getImageUrl());
//        Glide.with(mContext)
//                .load(att.getImageUrl())
//                .into(viewHolder.aImage);
            //然后定义RequestOptions:
            RequestOptions options = new RequestOptions()
//                    .placeholder(R.drawable.hainansanya)
//                    .error(R.drawable.hainansanya)
                    .priority(Priority.HIGH);
            //调用glide显示图片：
            Glide.with(mContext).load(att.getImageUrl()).apply(options).into(viewHolder.aImage);
        }

        viewHolder.aName .setText(att.getName());
        viewHolder.aAddress.setText(att.getAddress());
        viewHolder.aAddressMore .setText(att.getAddressMore());
        viewHolder.aContent .setText(att.getContent());
if(att.getImageUrl()==null){
    viewHolder.aLL.setVisibility(View.GONE);
}
        return view;

    }
    public Bitmap getLocalOrNetBitmap(String url)
    {
        Bitmap  bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a_1);
        if (url != null) {
            InputStream in = null;
            BufferedOutputStream out = null;
            try
            {
                //读取图片输入流
                in = new BufferedInputStream(new URL(url).openStream(), 2 * 1024);
                //准备输出流
                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream, 2 * 1024);
                byte[] b = new byte[1024];
                int read;
                //将输入流变为输出流
                while ((read = in.read(b)) != -1) {
                    out.write(b, 0, read);
                }
                out.flush();
                //将输出流转换为bitmap
                byte[] data = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                data = null;
                return bitmap;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return bitmap;
            }
        }
        return bitmap;
    }

    private void asyncloadImage(final ImageView imageView, final String uri) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (imageView != null && uri != null) {
                        imageView.setImageBitmap(bitmap);

                    }

                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //这个URI是图片下载到本地后的缓存目录中的URI
                    if (uri != null ) {
                        Bitmap bitmap = getLocalOrNetBitmap(uri);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = bitmap;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}


class ViewHolder{      //当布局加载过后，保存获取到的控件信息。

    TextView aName;
    TextView aAddress;
    TextView aAddressMore;
    TextView aContent;
    ImageView aImage;
    LinearLayout aLL;
}

