alter table widget add tags jsonb not null default '[]'::jsonb;
update widget set tags = '[]'::jsonb;
