package com.osama.project34.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.osama.project34.R;

/**
 * Created by bullhead on 9/9/17.
 *
 */

public class MailListTouchHelper extends ItemTouchHelper.SimpleCallback{
    private Paint p;
    private Context mContext;
    private OnSwiped callback;
    public MailListTouchHelper(Context context,OnSwiped callback){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        p=new Paint();
        this.mContext=context;
        this.callback=callback;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction==ItemTouchHelper.LEFT){
            callback.toLeft(viewHolder.getAdapterPosition());
        }
        if (direction==ItemTouchHelper.RIGHT){
            callback.toRight(viewHolder.getAdapterPosition());
        }
    }
    @Override
    public void onChildDraw(
            Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int   actionState,
            boolean isCurrentlyActive) {
        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height  = (float) itemView.getBottom() - (float) itemView.getTop();
            float width   = height / 3;

            if(dX > 0){
                p.setColor(Color.parseColor("#388E3C"));
                RectF background = new RectF((float) itemView.getLeft(),
                        (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_show_mail);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,
                        (float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,
                        (float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            } else {
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX,
                        (float) itemView.getTop(),(float) itemView.getRight(),
                        (float) itemView.getBottom());
                c.drawRect(background,p);

                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_white_48dp);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,
                        (float) itemView.getTop() + width,
                        (float) itemView.getRight() - width,(float)itemView.getBottom() - width);

                c.drawBitmap(icon,null,icon_dest,p);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
    public interface OnSwiped{
        void toLeft(int position);
        void toRight(int position);
    }

}
