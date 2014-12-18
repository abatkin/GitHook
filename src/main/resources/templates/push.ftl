<#escape x as x?html>
<html>
	<head></head>
	<body>
		<h1>${commits?size} new commits in ${repository.full_name}</h1>
	<ul>
		<#list commits as commit>
			<li>${commit.message?split("\n")?first} by ${(commit.author.name)!"Unknown"} at ${(commit.timestamp)!"Unknown"} <a href="${commit.url}">${commit.id}</a> (+${(commit.added?size)!0}, -${(commit.removed?size)!0}, ~${(commit.modified?size)!0})</li>
		</#list>
	</ul>
	</body>
</html>
</#escape>
