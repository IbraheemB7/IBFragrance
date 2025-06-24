package IBFragrance.Badran.ibfragrance.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import IBFragrance.Badran.ibfragrance.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnItemRemoveListener {
        void onItemRemove(int position);
    }

    public interface OnItemAddListener {
        void onItemAdd(int position);
    }

    private Context context;
    private List<Perfume> items;
    private boolean isCartScreen = false;
    private Set<String> cartIds = new HashSet<>(); // معرفات العطور في السلة

    private OnItemRemoveListener removeListener;
    private OnItemAddListener addListener;

    public CartAdapter(Context context, List<Perfume> items, Set<String> cartIds, boolean isCartScreen) {
        this.context = context;
        this.items = items;
        this.isCartScreen = isCartScreen;
        this.cartIds = cartIds;
    }

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        this.removeListener = listener;
    }

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.addListener = listener;
    }

    public void updateCartIds(Set<String> newCartIds) {
        this.cartIds = newCartIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Perfume perfume = items.get(position);

        holder.tvName.setText(perfume.getName());
        holder.tvPrice.setText(perfume.getPrice());

        Glide.with(context)
                .load(perfume.getImageUrl())
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.ivImage);

        if (isCartScreen) {
            // شاشة السلة
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.btnRemoveFromCart.setVisibility(View.VISIBLE);

            holder.btnRemoveFromCart.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onItemRemove(position);
                }
            });

        } else {
            // شاشة المنتجات
            boolean isInCart = cartIds.contains(perfume.getId());

            holder.btnAddToCart.setVisibility(isInCart ? View.GONE : View.VISIBLE);
            holder.btnRemoveFromCart.setVisibility(isInCart ? View.VISIBLE : View.GONE);

            holder.btnAddToCart.setOnClickListener(v -> {
                if (addListener != null) {
                    addListener.onItemAdd(position);
                } else {
                    Toast.makeText(context, perfume.getName() + " تم إضافته للسلة", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnRemoveFromCart.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onItemRemove(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;
        Button btnAddToCart, btnRemoveFromCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivCartImage);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }
}
