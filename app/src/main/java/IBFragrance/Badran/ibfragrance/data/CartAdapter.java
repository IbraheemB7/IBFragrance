package IBFragrance.Badran.ibfragrance.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import IBFragrance.Badran.ibfragrance.R;
import IBFragrance.Badran.ibfragrance.data.Perfume;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Perfume> cartItems;

    // منشئ الكلاس
    public CartAdapter(Context context, List<Perfume> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    // إنشاء الـ ViewHolder لكل عنصر في القائمة
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    // ربط البيانات بعناصر الواجهة في كل عنصر بالقائمة
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Perfume perfume = cartItems.get(position);

        holder.tvName.setText(perfume.getName());
        holder.tvPrice.setText(perfume.getPrice());

        // تحميل صورة العطر باستخدام Glide
        Glide.with(context)
                .load(perfume.getImageUrl())
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.ivImage);
    }

    // عدد عناصر القائمة
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // تعريف ViewHolder للعناصر
    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivCartImage);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
        }
    }
}
