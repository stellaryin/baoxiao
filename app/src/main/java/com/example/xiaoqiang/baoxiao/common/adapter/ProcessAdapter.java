package com.example.xiaoqiang.baoxiao.common.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.RadiusViewHelper;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpanUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.VectorDrawableUtils;

/**
 * Created:2018/4/27
 * Desc:流程适配器
 */
public class ProcessAdapter extends BaseQuickAdapter<ProcessEntity, BaseViewHolder> {
    private boolean isOperate = false;//是否可操作

    public ProcessAdapter(boolean isOperate) {
        super(R.layout.item_process);
        this.isOperate = isOperate;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcessEntity item) {
        if (isOperate) {
            helper.setVisible(R.id.item_process_operate_l, true);
        }

        if ((item.getProcessType() == FastConstant.PROCESS_TYPE_ONE && item.getPoint() == FastConstant.PROCESS_POINT_FIVE)
                || (item.getProcessType() == FastConstant.PROCESS_TYPE_TWO && item.getPoint() == FastConstant.PROCESS_POINT_FIVE)
                || (item.getProcessType() == FastConstant.PROCESS_TYPE_THREE && item.getPoint() == FastConstant.PROCESS_POINT_FOUR)) {
            helper.setText(R.id.item_process_approval_tv, "确认收款");
            helper.setVisible(R.id.item_process_reject_tv, false);
        } else {
            helper.setText(R.id.item_process_approval_tv, "批准");
            helper.setVisible(R.id.item_process_reject_tv, true);
        }

        //查看参考价 是否显示
        LinearLayout reference_price_l = helper.getView(R.id.item_process_reference_price_l);
        if (item.isTravel()) {
            helper.addOnClickListener(R.id.item_process_reference_price_l);
            reference_price_l.setVisibility(View.VISIBLE);
        } else {
            reference_price_l.setVisibility(View.GONE);
        }

        //设置标签
        ImageView sealImg = helper.getView(R.id.item_process_seal);
        LinearLayout rejectLayout = helper.getView(R.id.reimbursement_reject_reason_l);


        String pointInfo;
        if (item.getPoint() == FastConstant.PROCESS_POINT_ONE) {
            pointInfo = SpManager.getInstance().getUserInfo().getUser().getRealname() + "(我)";
        } else {
            pointInfo = SpManager.getInstance().getPointInfo(item.getPoint(), item.getProcessType());
        }
        if (item.getReject()) {
            sealImg.setVisibility(View.VISIBLE);
            rejectLayout.setVisibility(View.VISIBLE);
            helper.setText(R.id.reimbursement_reject_reason_tv, new SpanUtil()
                    .appendImage(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_process_failed, R.color.colorRed))
                    .append("  驳回原因：")
                    .append(item.getPointList().get(item.getPointList().size() - 2).getRemark())
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.colorRed)).create());

            pointInfo = SpManager.getInstance().getUserInfo().getUser().getRealname() + "(我)";

            sealImg.setImageResource(R.drawable.ic_process_reject);
        } else {
            rejectLayout.setVisibility(View.GONE);
            if (item.getPoint() == FastConstant.PROCESS_POINT_FINISH) {
                sealImg.setVisibility(View.VISIBLE);
                sealImg.setImageResource(R.drawable.ic_process_finish);
            } else {
                sealImg.setVisibility(View.GONE);
            }
        }

        //参数处理 填充
        CharSequence titleCs = new SpanUtil().append("申请事由：")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.txt_black1))
                .append(item.getReason()).create();
        helper.addOnClickListener(R.id.item_process_reject_tv)
                .addOnClickListener(R.id.item_process_approval_tv);
        helper.setText(R.id.process_title_tv, titleCs)
                .setText(R.id.process_founder_tv, item.getCreatorName() + "(" + SpManager.getInstance().mPositionManager.get(item
                        .getPosition()) + ")")
                .setText(R.id.process_accept_time_tv, TimeFormatUtil.string2Date2String(item.getCreatedAt()))
                .setText(R.id.process_point_tv, pointInfo)
                .setText(R.id.process_point_status_tv, item.getPointList().get(item.getPointList().size() - 1).getPointStatus() == 1 ?
                        "未操作" : "已处理")
                .setText(R.id.process_apply_amount_tv, NumberFormatterUtil.formatMoneyWithSelfValue(item.getAmount() + ""));

        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
