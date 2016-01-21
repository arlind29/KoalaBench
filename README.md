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

Usage instructions
------------------

The data generation can be called through the DBGen Java class.  If you invoke: 

java DBGen 

It generates data with the default parameters: the .tab data format, snow flake data model and scale factor sf=1. 

Below, we list the invocation options: 
      - Format: To specify a data format it is enough to add one of the values csv, json, xml or tab. The default value is tab. 
      - Data model: To specify a data model it is enough to add one of the values snow, star, flat or sparse. The default value is snow. 
      - Scale factor: To specify the scale factor you have to input sfX with X the value of the scale factor. For instance, sf10 stands for scale factor 10. The default scale factor is 1. 
      - Output folder: To generate data in another location in the file system, please specify the absolute directory location preceded by the symbol > without a space in between e.x. >/usr/local/data/tmp_dir/ 
      - Output HDFS folder: To generate data in the Hadoop file system, please specify the absolute hdfs file location preceded by the symbol > without a space in between e.x. >hdfs://nn1.example.com/user/hadoop/dir

For instance

      java DBGen json flat sf25 

will generate data in the flat data model with json data format and scale factor sf=25. 

And

      java DBGen snow sf10 csv

will generate data in the snowflake data model with csv data format and scale factor sf=10. 


The order of the parameters is not important. 

More details 
--------------
The code is written in java. It depends on the following libraries: 
- guava-18.0 

We recommend to open it with Eclipse and analyze the code structure. 

Documentation
-------------

The documentation is available on the following links: 

- technical document: http://www.irit.fr/recherches/SIG/files/KoalaBench_technical_report.pdf
- TPC-H documentation: http://www.tpc.org/tpc_documents_current_versions/pdf/tpch2.17.1.pdf
- SSB documentation: http://www.cs.umb.edu/~poneil/StarSchemaB.PDF 

This is an ongoing work. There might be bugs and unsolved issues. 
