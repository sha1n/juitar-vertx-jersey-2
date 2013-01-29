Juitar-Vert.x
=============
This repository plays with REST resources that fulfills requests through asynchronous worker-queues.
The code relies on embedded Vert.x for HTTP server and Jersey as a REST framework.

The challenge is to be able to extend Jersey to allow the response to be written by the worker thread or another thread
which is not a web server IO handler thread.