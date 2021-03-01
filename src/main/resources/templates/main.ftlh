<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by tag">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary mb-3" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new Message
    </a>
    <div class="collapse" id="collapseExample">
        <div class="form-group mb-3">
            <form method="post" action="/main" enctype="multipart/form-data">
                <div class="form-group mb-3">
                    <input type="text" class="form-control" name="text" placeholder="Text">
                </div>
                <div class="form-group mb-3">
                    <input type="text" class="form-control" name="tag" placeholder="Tag">
                </div>
                <div class="form-group mb-3">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group mb-3">
                    <button type="submit" class="btn btn-primary " >Add</button>
                </div>
            </form>
        </div>
    </div>
    <div>List of Messages</div>
    <br>
    <div class="card-columns">
    <#list messages as message>
        <div class="card m-1">

                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top">
                </#if>

            <div class="m-2">
            <span>${message.text}</span>
            </div>
            <i>${message.tag}</i>
        <div class="card-footer text-muted">
            ${message.authorName}
        </div>
        </div>
    <#else>
        No message
    </#list>
    </div>
</@c.page>