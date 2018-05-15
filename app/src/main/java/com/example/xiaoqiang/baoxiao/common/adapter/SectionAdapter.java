package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.MainActivity;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.MySection;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SectionAdapter(Context context, int layoutResId, int sectionHeadResId, List<MySection> data) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.item_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {

        Glide.with(context).load(item.t.getUser().getPhotoPath()).into((CircleImageView) helper.getView(R.id.mian_item_head));
        helper.setText(R.id.item_main_name, item.t.getUser().getNickName());

        helper.setText(R.id.item_main_pos, SpManager.mPositionManager.get(item.t.getPosition()));
    }
}
