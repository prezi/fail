# `override_dns` sapper

`start` writes a line into `/etc/hosts`; `stop` removes it.

Arguments to both scripts:

 * `$1`: DNS name. Required.
 * `$2`: IP address. Optional, defaults to `127.0.0.2`
