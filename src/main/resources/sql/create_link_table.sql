CREATE TABLE IF NOT EXISTS public.link
(
    id integer PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    link_address character varying COLLATE pg_catalog."default" NOT NULL,
    download_limit integer CHECK ( download_limit >= 0 ),
    lifetime timestamp without time zone,
    request_per_second integer CHECK ( request_per_second > 0 )
)