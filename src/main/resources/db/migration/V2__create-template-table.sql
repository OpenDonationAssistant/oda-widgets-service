create table template(
  id varchar(255) not null,
	owner_id varchar(255) not null,
  widget_type varchar(255) not null,
  showcase varchar(255) not null,
	properties jsonb
)
