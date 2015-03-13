# Introduction #

In DLNA, there are many network listen handlers such like SOAP server & SSDP server.
So, we need to define the base structure & sequences of listen handler.

# Details #

Simply, if you want to make a server thread, you need 3 step process.

1. **Make your own network protocol listener.**
> Network protocol listener should be implemented with com.elevenquest.sol.upnp.network.CommonListener.
> In this class, you should implement "listen" method.
> For more detail information, refer to the remark of CommonListener class.