package com.fourspaces.couchdb;

public class AdHocView extends View {
	public AdHocView(Database database, String function) {
		super(database, null,"_temp_view");
		this.function=function;
	}
	public String getFullName() {
		return "_temp_view";
	}

}
