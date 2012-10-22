package com.github.andlyticsproject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.andlyticsproject.model.Comment;
import com.github.andlyticsproject.model.CommentGroup;
import com.github.andlyticsproject.util.Utils;

public class CommentsListAdapter extends BaseExpandableListAdapter {

	private static final int TYPE_COMMENT = 0;
	private static final int TYPE_REPLY = 1;

	private LayoutInflater layoutInflater;

	private ArrayList<CommentGroup> commentGroups;

	private CommentsActivity context;


	public CommentsListAdapter(CommentsActivity activity) {
		this.setCommentGroups(new ArrayList<CommentGroup>());
		this.layoutInflater = activity.getLayoutInflater();
		this.context = activity;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {

		final Comment comment = getChild(groupPosition, childPosition);
		ViewHolderChild holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(comment.isReply() ? R.layout.comments_list_item_reply
					: R.layout.comments_list_item_comment, null);

			holder = new ViewHolderChild();
			holder.text = (TextView) convertView.findViewById(R.id.comments_list_item_text);
			holder.user = (TextView) convertView.findViewById(R.id.comments_list_item_username);
			holder.date = (TextView) convertView.findViewById(R.id.comments_list_item_date);
			holder.device = (TextView) convertView.findViewById(R.id.comments_list_item_device);
			holder.rating = (RatingBar) convertView.findViewById(R.id.comments_list_item_app_ratingbar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderChild) convertView.getTag();
		}

		holder.text.setText(comment.getText());
		if (comment.isReply()) {
			holder.date.setText(Utils.formatDate(comment.getReplyDate()));
		} else {
			holder.user.setText(comment.getUser());
			String version = comment.getAppVersion();
			String device = comment.getDevice();
			String deviceText = "";
			// building string: version X on device: XYZ
			if (isNotEmptyOrNull(version)) {
				if (isNotEmptyOrNull(device)) {
					deviceText = context.getString(R.string.comments_details_full, version, device);
				} else {
					deviceText = context.getString(R.string.comments_details_version, version);
				}
			} else if (isNotEmptyOrNull(device)) {
				deviceText = context.getString(R.string.comments_details_device, device);
			}
			if (isNotEmptyOrNull(deviceText)) {
				holder.device.setVisibility(View.VISIBLE);
				holder.device.setText(deviceText);
			} else {
				holder.device.setVisibility(View.GONE);
			}

			int rating = comment.getRating();
			if (rating > 0 && rating <= 5) {
				holder.rating.setRating((float) rating);
			}
		}

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				String text = comment.getText();
				String displayLanguage = Locale.getDefault().getLanguage();
				String url = "http://translate.google.de/m/translate?hl=<<lang>>&vi=m&text=<<text>>&langpair=auto|<<lang>>";

				try {
					url = url.replaceAll("<<lang>>", URLEncoder.encode(displayLanguage, "UTF-8"));
					url = url.replaceAll("<<text>>", URLEncoder.encode(text, "UTF-8"));
					Log.d("CommentsTranslate", "lang: " + displayLanguage + " url: " + url);

					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					context.startActivity(i);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		return convertView;
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		return getChild(groupPosition, childPosition).isReply() ? TYPE_REPLY : TYPE_COMMENT;
	}



	@Override
	public int getChildTypeCount() {
		return 2;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {

		ViewHolderGroup holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.comments_list_item_group_header, null);
			holder = new ViewHolderGroup();
			holder.date = (TextView) convertView.findViewById(R.id.comments_list_item_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}

		CommentGroup commentGroup = getGroup(groupPosition);
		holder.date.setText(Utils.formatDate(commentGroup.getDate()));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	private boolean isNotEmptyOrNull(String str) {
		return str != null && str.length() > 0;
	}

	static class ViewHolderGroup {
		TextView date;
	}

	static class ViewHolderChild {
		TextView text;
		RatingBar rating;
		TextView user;
		TextView date;
		TextView device;
	}

	@Override
	public int getGroupCount() {
		return getCommentGroups().size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getCommentGroups().get(groupPosition).getComments().size();
	}

	@Override
	public CommentGroup getGroup(int groupPosition) {
		return getCommentGroups().get(groupPosition);
	}

	@Override
	public Comment getChild(int groupPosition, int childPosition) {
		return getCommentGroups().get(groupPosition).getComments().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void setCommentGroups(ArrayList<CommentGroup> commentGroups) {
		this.commentGroups = commentGroups;
	}

	public ArrayList<CommentGroup> getCommentGroups() {
		return commentGroups;
	}

}
