create table widget(
  id varchar(255) not null,
	display_name varchar(255) not null,
	owner_id varchar(255) not null,
  widget_type varchar(255) not null,
  sort_order smallint not null,
	config jsonb
)
