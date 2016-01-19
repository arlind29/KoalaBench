# KoalaBench
A big data benchmark for decision support systems based on TPC-H

The Koala Big Data Bench is a benchmark data generator for testing decision support systems in the Big Data trend. It extends the TPC-H benchmark, well-known decision support benchmark. Some of the modifications are inspired from the SSB benchmark. The existing benchmarks are not compatible to NoSQL systems; they are conceived to work with relational databases. The new benchmark adapts to most database solutions (RDBMS and NoSQL). 

It generates data in different formats. It can support: 

- NoSQL systems
- relational databases

Data can be generated in different file formats: .tab, .csv, .json and .xml. 

Data can be generated to follow different conceptual logical schemas for data warehousing: 
- snow flake logical model 
      - with schema as in image:  http://www.irit.fr/recherches/SIG/files/model_for_snow.pdf 
      - with instances like the following JSON: http://www.irit.fr/recherches/SIG/files/instance_snow.json 
- star logical model 
      - with schema as in image:  http://www.irit.fr/recherches/SIG/files/model_for_star.pdf 
      - with instances like the following JSON: http://www.irit.fr/recherches/SIG/files/instance_star.json 
- flat model 
      - with schema as in image:  http://www.irit.fr/recherches/SIG/files/model_for_flat.pdf 
      - with instances like the following JSON: http://www.irit.fr/recherches/SIG/files/instance_flat.json 
- sparse vector 
      - with schema as in image:  http://www.irit.fr/recherches/SIG/files/model_for_sparse_order.pdf 
      - with instances like the following JSON: http://www.irit.fr/recherches/SIG/files/instance_sparse.json 


The code is written in java. It depends on the following libraries: 
- guava-18.0 

We recommend to open it with Eclipse and analyze the code structure. 

The documentation is available on the following links: 

- technical document: http://www.irit.fr/recherches/SIG/files/koala_bench_url_to_come.pdf
- TPC-H documentation: http://www.tpc.org/tpc_documents_current_versions/pdf/tpch2.17.1.pdf
- SSB documentation: http://www.cs.umb.edu/~poneil/StarSchemaB.PDF 

This is an ongoing work. There might be bugs and unsolved issues. 
