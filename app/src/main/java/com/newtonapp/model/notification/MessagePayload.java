package com.newtonapp.model.notification;

import com.google.gson.annotations.SerializedName;

public class MessagePayload{

	@SerializedName("image")
	private String image;

	@SerializedName("action")
	private String action;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	@SerializedName("action_destination")
	private String actionDestination;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setAction(String action){
		this.action = action;
	}

	public String getAction(){
		return action;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setActionDestination(String actionDestination){
		this.actionDestination = actionDestination;
	}

	public String getActionDestination(){
		return actionDestination;
	}

	@Override
 	public String toString(){
		return 
			"MessagePayload{" + 
			"image = '" + image + '\'' + 
			",action = '" + action + '\'' + 
			",title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			",action_destination = '" + actionDestination + '\'' + 
			"}";
		}
}