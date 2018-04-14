package com.example.bharg_000.instructorsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BAngadi on 4/5/2018.
 */

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Course> courseList;

    public CourseRecyclerAdapter(Context context,List<Course> courseList){
        this.context = context;
        this.courseList = courseList;
    }
    @Override
    public CourseRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(CourseRecyclerAdapter.ViewHolder holder, int position) {
        Course course= courseList.get(position);
        String imageUrl = null;
        holder.title.setText(course.getTitle());
        holder.desc.setText(course.getDesc());
        holder.show.setText(course.getShow());
        imageUrl = course.getImage();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc;
        public ImageView image;
        public Button show;
        String userid;
        public ViewHolder(View view,Context ctx) {
            super(view);
            context =ctx;
            title = (TextView) view.findViewById(R.id.coursenameList);
            desc=(TextView) view.findViewById(R.id.courseDescList) ;
            image= (ImageView) view.findViewById(R.id.postImageList);
            show=(Button) view.findViewById(R.id.addShowList);

            userid=null;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //we can go to next activity
                }
            });
        }
    }
}
