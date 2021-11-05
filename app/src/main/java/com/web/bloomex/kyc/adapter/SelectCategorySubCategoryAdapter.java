package com.web.bloomex.kyc.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.web.bloomex.R;
import com.web.bloomex.kyc.VerifyCompleteSubmitKycScreen;
//import com.web.bloomex.kyc.VerifyKycForPersonalInfoScreen;

import org.json.JSONArray;
import org.json.JSONObject;


public class SelectCategorySubCategoryAdapter extends RecyclerView.Adapter<SelectCategorySubCategoryAdapter.MyViewHolder> {

    private JSONArray datAr;
    private AppCompatActivity pActivity;
    private String imageUrl="";
    private  CheckBox commonChekBox;
    private String type="";

    public SelectCategorySubCategoryAdapter(JSONArray ar, AppCompatActivity paActiviity) {
        datAr = ar;
        pActivity = paActiviity;
    }
    public SelectCategorySubCategoryAdapter(JSONArray ar, AppCompatActivity paActiviity,String type) {
        datAr = ar;
        pActivity = paActiviity;
        this.type=type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_best_restaurant;
        ImageView category_image;
        TextView category_name;
        CheckBox chk_selectcategory;

        public MyViewHolder(View view) {
            super(view);


            ll_best_restaurant = view.findViewById(R.id.ll_best_restaurant);
            category_image = view.findViewById(R.id.category_image);
            category_name = view.findViewById(R.id.category_name);
            chk_selectcategory = view.findViewById(R.id.chk_selectcategory);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_category_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(pActivity instanceof VerifyCompleteSubmitKycScreen)
        {
            try {
                JSONObject object = (JSONObject) datAr.get(position);
                String type = object.getString("name");
                holder.category_name.setText(type);
                holder.category_image.setVisibility(View.GONE);
                holder.chk_selectcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try
                        {
                            if (commonChekBox != null) {
                                commonChekBox.setChecked(false);
                            }
                            commonChekBox = holder.chk_selectcategory;
                            ((VerifyCompleteSubmitKycScreen) pActivity).setKycType(type);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
       // if(pActivity instanceof VerifyKycForPersonalInfoScreen)
        {
            if(type.equals("doc")){
                try {
                    JSONObject object = (JSONObject) datAr.get(position);
                    String type = object.getString("name");
                    holder.category_name.setText(type);
                    holder.category_image.setVisibility(View.GONE);
                    holder.chk_selectcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            try
                            {
                                if (commonChekBox != null) {
                                    commonChekBox.setChecked(false);
                                }
                                commonChekBox = holder.chk_selectcategory;
                           //     ((VerifyKycForPersonalInfoScreen) pActivity).setDocType(type);

                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                try {
                    JSONObject object = (JSONObject) datAr.get(position);
                    String type = object.getString("name");
                    holder.category_name.setText(type);
                    holder.category_image.setVisibility(View.GONE);
                    holder.chk_selectcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            try
                            {
                                if (commonChekBox != null) {
                                    commonChekBox.setChecked(false);
                                }
                                commonChekBox = holder.chk_selectcategory;
                               // ((VerifyKycForPersonalInfoScreen) pActivity).setKycType(type);

                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


        }


    }

    @Override
    public int getItemCount() {
        return datAr.length();
    }


    private void showImage(final String url, final ImageView header_img) {
        pActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(pActivity)
                        .load(url)
                        .placeholder(R.drawable.no_data)
                       // .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });


    }



//            "category_id": "7",
//                    "category_name": "test4",
//                    "category_icon": "",
//                    "parent_id": "1",
//                    "status": "1"
}