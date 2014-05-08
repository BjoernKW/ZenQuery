CREATE TABLE database_connections (
    id integer NOT NULL,
    name character varying NOT NULL,
    url character varying NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL
);

--
-- TOC entry 170 (class 1259 OID 16770)
-- Name: database_connections_id_seq; Type: SEQUENCE; Schema: public; Owner: willy
--

CREATE SEQUENCE database_connections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2238 (class 0 OID 0)
-- Dependencies: 170
-- Name: database_connections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: willy
--

ALTER SEQUENCE database_connections_id_seq OWNED BY database_connections.id;


--
-- TOC entry 173 (class 1259 OID 16783)
-- Name: queries; Type: TABLE; Schema: public; Owner: willy; Tablespace: 
--

CREATE TABLE queries (
    id integer NOT NULL,
    key character varying NOT NULL,
    database_connection_id integer NOT NULL
);

--
-- TOC entry 172 (class 1259 OID 16781)
-- Name: queries_id_seq; Type: SEQUENCE; Schema: public; Owner: willy
--

CREATE SEQUENCE queries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2239 (class 0 OID 0)
-- Dependencies: 172
-- Name: queries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: willy
--

ALTER SEQUENCE queries_id_seq OWNED BY queries.id;


--
-- TOC entry 175 (class 1259 OID 16794)
-- Name: query_versions; Type: TABLE; Schema: public; Owner: willy; Tablespace: 
--

CREATE TABLE query_versions (
    id integer NOT NULL,
    content character varying NOT NULL,
    version integer NOT NULL,
    is_current_version boolean NOT NULL,
    query_id integer NOT NULL
);

--
-- TOC entry 174 (class 1259 OID 16792)
-- Name: query_versions_id_seq; Type: SEQUENCE; Schema: public; Owner: willy
--

CREATE SEQUENCE query_versions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 2240 (class 0 OID 0)
-- Dependencies: 174
-- Name: query_versions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: willy
--

ALTER SEQUENCE query_versions_id_seq OWNED BY query_versions.id;


--
-- TOC entry 2104 (class 2604 OID 16775)
-- Name: id; Type: DEFAULT; Schema: public; Owner: willy
--

ALTER TABLE ONLY database_connections ALTER COLUMN id SET DEFAULT nextval('database_connections_id_seq'::regclass);


--
-- TOC entry 2105 (class 2604 OID 16786)
-- Name: id; Type: DEFAULT; Schema: public; Owner: willy
--

ALTER TABLE ONLY queries ALTER COLUMN id SET DEFAULT nextval('queries_id_seq'::regclass);


--
-- TOC entry 2106 (class 2604 OID 16797)
-- Name: id; Type: DEFAULT; Schema: public; Owner: willy
--

ALTER TABLE ONLY query_versions ALTER COLUMN id SET DEFAULT nextval('query_versions_id_seq'::regclass);

--
-- TOC entry 2108 (class 2606 OID 16780)
-- Name: database_connections_pkey; Type: CONSTRAINT; Schema: public; Owner: willy; Tablespace: 
--

ALTER TABLE ONLY database_connections
    ADD CONSTRAINT database_connections_pkey PRIMARY KEY (id);


--
-- TOC entry 2110 (class 2606 OID 16791)
-- Name: queries_pkey; Type: CONSTRAINT; Schema: public; Owner: willy; Tablespace: 
--

ALTER TABLE ONLY queries
    ADD CONSTRAINT queries_pkey PRIMARY KEY (id);


--
-- TOC entry 2113 (class 2606 OID 16802)
-- Name: query_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: willy; Tablespace: 
--

ALTER TABLE ONLY query_versions
    ADD CONSTRAINT query_versions_pkey PRIMARY KEY (id);


--
-- TOC entry 2111 (class 1259 OID 16808)
-- Name: unique_key; Type: INDEX; Schema: public; Owner: willy; Tablespace: 
--

CREATE UNIQUE INDEX unique_key ON queries USING btree (key);


--
-- TOC entry 2114 (class 2606 OID 16803)
-- Name: queries_database_connection_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: willy
--

ALTER TABLE ONLY queries
    ADD CONSTRAINT queries_database_connection_id_fkey FOREIGN KEY (database_connection_id) REFERENCES database_connections(id);


--
-- TOC entry 2115 (class 2606 OID 16809)
-- Name: query_versions_query_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: willy
--

ALTER TABLE ONLY query_versions
    ADD CONSTRAINT query_versions_query_id_fkey FOREIGN KEY (query_id) REFERENCES queries(id);
