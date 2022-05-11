package com.example.meloady;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.remote.GrpcCallProvider;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {

    private List<Room> roomList;
    private Context context;

    public RoomsAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.roomName.setText(roomList.get(position).getName());
        holder.roomId.setText(roomList.get(position).getId());
        holder.roomType.setText((roomList.get(position).getPublic()?"Public":"Private"));

        //conseguir el id de la room de firebase y pasarlo por el adapter al my room fragment

        holder.roomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomFragment roomFragment = RoomFragment.newInstance(
                        roomList.get(holder.getAdapterPosition()).getName(),
                        roomList.get(holder.getAdapterPosition()).getId()

                       // roomList.get(holder.getAdapterPosition()).getId()
                );
                //poner bundle para usar el mismo metodo para recoger el id???
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,roomFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        TextView roomId;
        TextView roomType;
        CardView roomLayout;
        public MyViewHolder(View view) {
            super(view);
            roomLayout = view.findViewById(R.id.roomLayout);
            roomName = view.findViewById(R.id.nameRow);
            roomId = view.findViewById(R.id.idRow);
            roomType = view.findViewById(R.id.isPublicRoom);
        }
    }
}
