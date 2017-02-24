package com.hisense.checksquare.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hisense.checksquare.R;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckService;

import java.util.List;


public class ItemClickAdapter extends BaseMultiItemQuickAdapter<CheckEntity, BaseViewHolder> {

    private SparseArray<String> indexArr;
    private Context mContext;

    public ItemClickAdapter(Context context, List<CheckEntity> data) {
        super(data);
        mContext = context;
        addItemType(CheckEntity.TYPE_ITEM_VIEW_HW, R.layout.item_view_hw);
        addItemType(CheckEntity.TYPE_ITEM_VIEW_FUNC, R.layout.item_view_hw);
        int size = data != null ? data.size() : 20;
        indexArr = new SparseArray<>(size);
    }

    public int getEntityPosition(CheckEntity entity){
        int index = indexArr.indexOfValue(entity.checkId);
        if (index >= 0) {
            return indexArr.keyAt(index);
        } else {
            return -1;
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CheckEntity item) {
        convertIndexById(helper, item);

        // image
        convertImage(helper, item);

        // name - title
        convertName(helper, item);

        // services - results
        convertResults(helper, item);
    }

    private void convertIndexById(BaseViewHolder helper, CheckEntity item) {
        int indexOfValue = indexArr.indexOfValue(item.checkId);
        if (indexOfValue >= 0) {
            indexArr.removeAt(indexOfValue);
        }
        indexArr.put(helper.getLayoutPosition() + this.getHeaderLayoutCount(), item.checkId);
    }

    private void convertName(BaseViewHolder helper, CheckEntity item) {
        helper.setText(R.id.tv_title, item.checkName);
    }

    private void convertResults(BaseViewHolder helper, CheckEntity item) {
        List<CheckService> checkServices = item.checkServices;
        if (checkServices != null && !checkServices.isEmpty()) {
            LinearLayout linearLayout = (LinearLayout) helper.getView(R.id.tv_result);

            int size = checkServices.size();
            for (int i = 0; i < size; i++) {
                View view = linearLayout.getChildAt(i);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setText(new StringBuilder(checkServices.get(i).toString()).append("\n"));
                } else {
                    TextView textView = new TextView(mContext);
                    textView.setText(new StringBuilder(checkServices.get(i).toString()).append("\n"));
                    textView.setTextColor(mContext.getResources().getColor(R.color.txt_color));
                    textView.setTextSize(13);
                    linearLayout.addView(textView);
                }
            }
        }
    }

    private void convertImage(BaseViewHolder helper, CheckEntity item) {
        if (CheckEntity.CHECK_STATUS_CHECKING.equalsIgnoreCase(item.checkStatus)) {
            helper.setImageResource(R.id.imageView, R.drawable.ic_event_search);
        } else if (CheckEntity.CHECK_STATUS_FAIL.equalsIgnoreCase(item.checkStatus)) {
            helper.setImageResource(R.id.imageView, R.drawable.ic_event_unavailable);
        } else if (CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(item.checkStatus)) {
            helper.setImageResource(R.id.imageView, R.drawable.ic_event_available);
        } else {
            if (helper.getItemViewType() == CheckEntity.TYPE_ITEM_VIEW_HW) {
                helper.setImageResource(R.id.imageView, R.drawable.ic_settings);
            } else {
                helper.setImageResource(R.id.imageView, R.drawable.ic_funcs);
            }
        }
    }
}
